# Test REST-project
Build application by "`mvn clean package`"

By default application will create local file for H2 database and use it. 

If need to use postgres, build with command "`-Ppostgres`".

build: `mvn package`

run: `java -jar fuel-company-1.0.0-SNAPSHOT.jar`


# Save record

"application/json;charset=UTF-8"

POST: _http://localhost:8080/purchases_

         {
                "fuelType": "D",
                "volume":20,
                "price": 3.25,
                "driverId": 1,
                "date": "2037-09-09"
            }
            
# Save records from file
file / "multipart/form-data;boundary="234" 

POST: _http://localhost:8080/purchases/file_

example file multipart.json in applications test resources


# Total spent amount of money grouped by month
Can add a web request parameter: driverId (?driverId=2)

GET: _http://localhost:8080/reports/amount_


# List fuel consumption records for specified month 
each row should contain: fuel type, volume, date, price, total price, driver ID

Can add a web request parameters: driverId and year (?driverId=2&year=2018)

GET: _http://localhost:8080/reports/months/6_


# Statistics for each month
list fuel consumption records grouped by fuel type (each row should contain:fuel type, volume, average price, total price)

Can add a web request parameters: driverId and year (?driverId=2&year=2018)

GET: _http://localhost:8080/reports/consumption_
