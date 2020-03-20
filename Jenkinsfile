#!/usr/bin/env groovy

pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }
    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.6-jdk-8'
                    label 'docker && linux'
                }
            }
            steps {
                // sh 'mvn --batch-mode --errors --fail-at-end --show-version clean test || 0'
                sh 'mvn clean verify || 0'
                junit '**/target/**/*.xml'
            }
        }
    }
}
