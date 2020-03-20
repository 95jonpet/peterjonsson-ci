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
                    args '-v $HOME/.m2:/root/.m2'
                    label 'docker && linux'
                }
            }
            steps {
                sh 'mvn --batch-mode --errors --fail-at-end --show-version clean test || 0'
                junit '**/target/**/*.xml'

                // Load current library version for later use.
                library "peterjonsson-ci@${env.GIT_BRANCH ?: 'master'}"
            }
        }
        stage('Integration Tests') {
            parallel {
                stage('Rpmbuild') {
                    agent {
                        dockerfile {
                            filename 'Dockerfile'
                            dir 'examples/rpmbuild'
                        }
                    }
                    steps {
                        dir('examples/rpmbuild') {
                            rpmbuild(version: '1.0.0', release: '1', topdir: 'src/rpm', specfile: 'hello-world.spec')
                        }
                    }
                }
            }
        }
    }
}
