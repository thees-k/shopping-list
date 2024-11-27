
# Setup

## 1. Create a database file inside folder database:

```bash
cd database
touch shopping-list.mv.db
```

## 2. Start h2 manually:
```
java -cp h2-<version>.jar org.h2.tools.Server -tcp
```

## 3. Start the App

EXPLAIN HERE HOW TO START THE APP

## 4. Optional: View database in webbrowser

Navigate to http://localhost:8080/h2-console/

Enter as JDBC URL: `jdbc:h2:tcp://localhost/~/workspace-sts4.26/shopping-list/database/shopping-list`

Username: sa

Password: (empty)


