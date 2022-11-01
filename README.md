# OpenNMS Velocloud Integration Plugin [![CircleCI](https://circleci.com/gh/OpenNMS/opennms-velocloud-plugin.svg?style=svg)](https://circleci.com/gh/OpenNMS/opennms-velocloud-plugin)


Build and install the plugin into your local Maven repository using:

```
mvn clean install
```

To speed up development iterations, one can use `-Dcodegen.skip=true` to skip re-generating the client model classes.


From the OpenNMS Karaf shell:
```
feature:repo-add mvn:org.opennms.plugins.velocloud/karaf-features/0.1.0-SNAPSHOT/xml
feature:install opennms-plugins-velocloud
```


```
cp assembly/kar/target/opennms-velocloud-plugin.kar /opt/opennms/deploy/
feature:install opennms-plugins-velocloud
```

```
bundle:watch *
```

You can also access the REST endpoint mounted by the plugin at `http://localhost:8980/opennms/rest/plugin/velocloud/`
