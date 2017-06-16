#!/bin/bash
# Starts the application assuming "mvn clean install" has completed successfully.

if ! [ -f target/msa-wetness-analyzer-*-SNAPSHOT.jar ]; then
    echo "You must 'mvn clean install' before starting the project." 1>&2
    exit 1
fi

mvn exec:java -Dexec.mainClass="com.sujitha.evariant.main.MSAWetnessAnalyzer"