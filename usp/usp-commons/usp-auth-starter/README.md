## 自定义 properties

```json
{
	"auth": {
		"httpUrls": [],
		"renew": {
			"enable": false,
			"exclusiveClientIds": [],
			"includeClientIds": [],
			"timeRatio": 0.5
		},
		"urlPermission": {
			"enable": false,
			"exclusiveClientIds": [],
			"ignoreUrls": [],
			"includeClientIds": []
		}
	},
	"code": {
		"ignoreClientCode": []
	},
	"ignore": {
		"httpUrls": [],
		"menusPaths": [],
		"urls": ["/oauth/**", "/actuator/**", "/*/v2/api-docs", "/swagger/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/druid/**"]
	}
}
```
