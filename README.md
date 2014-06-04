# Dropwizard Debian Package

A maven plugin for packaging your [Dropwizard Application](http://dropwizard.github.io/dropwizard) as a [Debian package](http://en.wikipedia.org/wiki/Deb_\(file_format\)). Your Application is managed as an [Upstart](http://upstart.ubuntu.com) job, with the service name `${project.artifactId}`.

During packaging your configuration file is treated as a [Mustache](http://mustache.github.io) template, and configuration properties injected. Parameters are exposes prefixed with both `dw` and `dropwizard`. Missing properties will cause the build to fail.
Assuming `validate` is set to true (the default), the configuration will be validated, and build failed if it fails.

[![Build Status](https://api.travis-ci.org/reines/dropwizard-debpkg-maven-plugin.png)](https://travis-ci.org/reines/dropwizard-debpkg-maven-plugin)

## Usage

### Sample plugin definition in project pom

    <plugin>
        <groupId>com.jamierf.dropwizard</groupId>
        <artifactId>dropwizard-debpkg-maven-plugin</artifactId>
        <version>${dropwizard-debpkg-maven-plugin.version}</version>
        <configuration>
            <configTemplate>${basedir}/src/config.yml</configTemplate>
            <jvm>
                <memory>2g</memory>
            </jvm>
            <dropwizard>
                <httpPort>8080</httpPort>
                <httpAdminPort>8081</httpAdminPort>
            </dropwizard>
        </configuration>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>dwpackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

### Sample Application configuration template

    # {{{project.name}}} - {{{project.description}}}
    # {{{project.artifactId}}} configuration

    server:
      applicationConnectors:
        - type: http
          port: {{{dw.httpPort}}}
      adminConnectors:
        - type: http
          port: {{{dw.httpAdminPort}}}

    logging:
      level: INFO

      appenders:
        - type: console
        - type: file
          currentLogFilename: "{{{path.logDirectory}}}/test.log"
          archive: false

## Configuration

Below is the default configuration. The only required parameter is the `configTemplate` path, however it is highly likely you will also require some `dropwizard` properties to be injected in to the service configuration.

    <configuration>
        <deb>
            <maintainer>Unspecified</maintainer><!-- Optional: The person responsible for this service. -->
        </deb>
        <jvm>
            <memory>128m</memory><!-- Optional: JVM heap size to allocate, once deployed. -->
            <packageName>openjdk-7-jdk</packageName><!-- Optional: JRE package to ensure installed as part of deployment. -->
            <packageVersion>latest</packageVersion><!-- Optional: Version of JRE package to require, defaults to the latest. -->
        </jvm>
        <unix>
            <user>dropwizard</user><!-- Optional: The unix user to create and run as. -->
        </unix>
        <pgp><!-- Optional: Not present by default. If present, all children are required. -->
            <alias /><!-- Required: Alias of PGP key to sign with. -->
            <keyring /><!-- Required: Path to PGP keyring file. -->
            <passphrase /><!-- Required: Pass phrase of PGP keyring. -->
        </pgp>
        <path>
            <jarFile>/usr/share/java/${project.artifactId}.jar</jarFile><!-- Optional: Path to the service jar, once deployed. -->
            <configFile>/etc/${project.artifactId}.yml</configFile><!-- Optional: Path to your service configuration, once deployed. -->
            <jvmConfigFile>/etc/${project.artifactId}.jvm.conf</jvmConfigFile><!-- Optional: Path to your JVM parameter configuration, once deployed. -->
            <logDirectory>/var/log/${project.artifactId}</logDirectory><!-- Optional: Directory for service logs, once deployed. -->
            <upstartFile>/etc/init/${project.artifactId}.conf</upstartFile><!-- Optional: Path to the service upstart configuration, once deployed. -->
        </path>
        <files><!-- Optional: Empty by default. -->
            <file>
                <source /><!-- Required: Path to the resource file. -->
                <target /><!-- Required: Path to the resource, once deployed. -->
                <filter>true</filter><!-- Optional: If this resource should be treated as a template. -->
                <user>${unix.user}</user><!-- Optional: The user the resource should be owned by. -->
                <mode>0600</mode><!-- Optional: The filemode for this resource. -->
            </file>
        </files>
        <dropwizard /><!-- Optional: Map of parameters to substitute in to your configuration template on packaging. -->
        <configTemplate /><!-- Required: Path to your service configuration template. -->
        <artifactFile>${project.build.directory}/${project.artifactId}-${project.version}.jar</artifactFile><!-- Optional: Path to the service jar to package. -->
        <outputFile>${project.build.directory}/${project.artifactId}-${project.version}.deb</outputFile><!-- Optional: The path to output the Debian package to. -->
        <validate>true</validate><!-- Optional: Enable validation of your service configuration at package time. -->
    </configuration>

## Change Log

### 0.3 - _Upcoming_

* Added support for including arbitrary files in the package using the `<files />` configuration parameter.
* Fixed configuration parameters not being respected properly.
* No longer delete the unix user on package uninstall, in-case we're using a user that we didn't create ourselves.
* No longer force the use of a 64-bit JVM.

### 0.2 - 4 June 2014

* Added JVM configuration with sensible defaults and GC logging.
* Replaced configuration validation implementation to avoid use of bytecode manipulation.
* Added ability to PGP encrypt packages using the `<pgp />` configuration parameter.

### 0.1 - 26 May 2014

* Initial release.

## License

Released under the [Apache 2.0 License](LICENSE).
