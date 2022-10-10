A shell command to test and examine client interactions.

## Installing
The feature is not installed by default. Use the following command to install it:
```
feature:install opennms-plugins-velocloud-client-shell
```

## Examples
```
opennms-velocloud:client \
  --url "https://vco134-usvi1.velocloud.net/" \
  --key "API_KEY" \
  all \
  enterpriseProxyGetEnterpriseProxyGateways \
  "{}"
```
