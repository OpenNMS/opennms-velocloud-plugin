## Writing the plugin's documentation

We use *Antora* to develop and build the documentation. See https://docs.antora.org/antora/latest/install-and-run-quickstart/
for a brief overview on how to install and use *Antora*.
You can also refer to https://docs.opennms.com/horizon/latest/write-the-docs/overview.html for details on how we develop our product documentation.

### Building the documentation

```
$ cd opennms-velocloud-plugin
$ antora generate local-site.yml
$ serve public
```

