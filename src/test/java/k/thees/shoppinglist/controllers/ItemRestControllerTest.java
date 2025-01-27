package k.thees.shoppinglist.controllers;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import k.thees.shoppinglist.model.Item;
import k.thees.shoppinglist.repositories.ItemRepositoryInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ItemRestControllerTest {

	@Autowired
	private ItemRepositoryInterface itemRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testGet() throws Exception {

		Item item = itemRepository.findAll().get(0);

		// mockMvc sends a mock GET request:
		MvcResult result = mockMvc.perform(get(ItemRestController.ITEMS_PATH_ID, item.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(buildMatcher("id", item.getId()))
				.andExpect(buildMatcher("text", item.getText()))
				.andExpect(buildMatcher("modifiedBy", item.getModifiedBy()))
				.andExpect(buildMatcher("modifiedAt", item.getModifiedAt()))
				.andExpect(buildMatcher("done", item.isDone()))
				.andReturn();

		// Get the JSON response as a string
		String jsonResponse = result.getResponse().getContentAsString();

		// Print out the JSON response
		log.debug("\nJSON Response:\n" + jsonResponse);

		// Print the formatted JSON
		log.debug("\n" + formatJson(jsonResponse) + "\n");
	}

	private ResultMatcher buildMatcher(String fieldName, Object object) {
		return jsonPath("$."+fieldName).value(object + "");
	}

	private String formatJson(String jsonResponse) throws JsonProcessingException, JsonMappingException {

		if (StringUtils.isBlank(jsonResponse)) {
			return "";
		} else {
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing

			// Parse the JSON string and format it
			Object json = objectMapper.readValue(jsonResponse, Object.class);
			return objectMapper.writeValueAsString(json);
		}

	}

	@Test
	void testGetButNotFound() throws Exception {

		mockMvc.perform(get(ItemRestController.ITEMS_PATH_ID, -1)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Test
	void testGetAll() throws Exception {

		int recordCount = (int) itemRepository.count();

		mockMvc.perform(get(ItemRestController.ITEMS_PATH)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()", is(recordCount)));

	}

	@Rollback
	@Transactional
	@Test
	void testGetAllReturnsEmptyList() throws Exception {

		itemRepository.deleteAll();

		assert(itemRepository.count() == 0);

		mockMvc.perform(get(ItemRestController.ITEMS_PATH)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()", is(0)));
	}

	@Rollback
	@Transactional
	@Test
	void testCreate() throws Exception {

		final Item item = Item.builder()
				.text("Apples")
				.modifiedAt(LocalDateTime.now())
				.modifiedBy("Peter")
				.done(false)
				.build();

		String json = objectMapper.writeValueAsString(item);

		log.debug(formatJson(json));

		MvcResult result = mockMvc
				.perform(post(ItemRestController.ITEMS_PATH)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn();

		// Print response headers
		log.debug("Response Headers:");
		result.getResponse().getHeaderNames().forEach(
				headerName -> log.debug(headerName + ": " + result.getResponse().getHeader(headerName)));

		// Print response body
		String responseJson = result.getResponse().getContentAsString();
		log.debug("Response Body:\n" + formatJson(responseJson));

		Integer id = fetchIdFromLocation(result);

		itemRepository.findById(id)
		.ifPresentOrElse(newCreatedBook -> validateItemCompletely(newCreatedBook, item, id), AssertionError::new);
	}

	private Integer fetchIdFromLocation(MvcResult result) {
		String pathOfLocation = result.getResponse().getHeaders("Location").get(0);
		String [] pathElements = pathOfLocation.split("/");
		String id = pathElements[pathElements.length-1];
		return Integer.valueOf(id);
	}

	@Rollback
	@Transactional
	@Test
	void testDelete() throws Exception {

		Item item = itemRepository.findAll().get(0);
		Integer id = item.getId();

		mockMvc.perform(delete(ItemRestController.ITEMS_PATH_ID, item.getId())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

		itemRepository.flush();

		assertTrue(itemRepository.findById(id).isEmpty());
	}

	@Rollback
	@Transactional
	@Test
	void testDeleteButNotFound() throws Exception {

		mockMvc.perform(delete(ItemRestController.ITEMS_PATH_ID, -1)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Rollback
	@Transactional
	@Test
	void testUpdate() throws JsonProcessingException, Exception {

		Item item = itemRepository.findAll().get(0);
		Integer id = item.getId();

		final Item updateItem = Item.builder()
				.text("Tea")
				.modifiedAt(LocalDateTime.now())
				.modifiedBy("Joan")
				.done(true)
				.build();

		mockMvc.perform(put(ItemRestController.ITEMS_PATH_ID, id)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateItem)))
		.andExpect(status().isNoContent());

		itemRepository.flush();

		item = itemRepository.findById(id).get();
		validateItemCompletely(item, updateItem, id);
	}

	private void validateItemCompletely(Item item, Item expectedItem, Integer expectedId) {

		assertEquals(item.getId(), expectedId);
		if(!item.equalsAnotherItem(expectedItem)) {
			throw new AssertionError();
		}
	}

	@Test
	void testUpdateButNotFound() throws Exception {

		final Item updateItem = Item.builder()
				.text("Coffee")
				.modifiedAt(LocalDateTime.now())
				.modifiedBy("Alan")
				.done(false)
				.build();

		mockMvc.perform(put(ItemRestController.ITEMS_PATH_ID, -1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateItem)))
		.andExpect(status().isNotFound());
	}

	@Rollback
	@Transactional
	@Test
	void testPatch() throws JsonProcessingException, Exception {

		LocalDateTime newModifiedAt = LocalDateTime.of(2030, 1, 2, 3, 4, 5, 6);

		final int itemId = 2;
		Item item = itemRepository.findById(itemId).get();

		assert(item.isDone() != true);
		assert(item.getModifiedAt().compareTo(newModifiedAt) != 0);

		var oldModifiedBy = item.getModifiedBy();
		var oldVersion = item.getVersion();
		var oldText = item.getText();

		Map<String, Object> map = new HashMap<>();
		map.put("done", true);
		map.put("modifiedAt", newModifiedAt);

		mockMvc.perform(patch(ItemRestController.ITEMS_PATH_ID, itemId)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(map)))
		.andExpect(status().isNoContent());

		itemRepository.flush();

		item = itemRepository.findById(itemId).get();

		assertEquals(oldVersion + 1, item.getVersion());
		assertEquals(oldModifiedBy, item.getModifiedBy());
		assertEquals(oldText, item.getText());

		assertEquals(newModifiedAt, item.getModifiedAt());
		assertTrue(item.isDone());
	}

	@Test
	void testPatchButNotFound() throws Exception {

		mockMvc.perform(patch(ItemRestController.ITEMS_PATH_ID, -1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Item.builder().build())))
		.andExpect(status().isNotFound());
	}

}
