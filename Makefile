.PHONY: build run verify clean test

build:
	./gradlew buildPlugin

run:
	./gradlew runIde

verify:
	./gradlew verifyPlugin

clean:
	./gradlew clean

test:
	./gradlew test
