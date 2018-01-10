# SeleniumUtil


## Have a try!
*You should have installed the lastest chrome in your computer*

- Run `./gradlew setupLocal` to setup local environment for selenium server.
- Run `./gradlew runSelenium` to run the selenium server.
- Run `./gradlew test` to start the sample of GUI tests.

### Using behind proxy
If you need proxy to download files in `setupLocal` task, run following commands 
instead of `gradlew setupLocal`:
``` bash
# proxy host 10.0.0.1, port 8118
# change it to your own
./gradlew setupLocal -PproxyHost=10.0.0.1 -PproxyPort=8118
```

## Selenium Docker
Run selenium grid with docker.
https://github.com/SeleniumHQ/docker-selenium
