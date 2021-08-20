# Data Service
This project is intended to practice developing REST APIs with JWT token-based access.

### Technology for this
* Language: Scala
* Web Framework: Spring Webflux, Spring Security
* Api Document: OpenApi3
* Jwt library: jjwt (Json Web Token)
* Data Storage: Redis [Window 10](https://github.com/microsoftarchive/redis/releases)

### JWT token for the test
* User Role
```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MjkxMTAwNzMsImV4cCI6MTY2MDY1MzM0MiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoidGVzdEBnbWFpbC5jb20iLCJyb2xlIjpbInVzZXIiXX0.Tqwu7GkP-L18_QlVM7FX_Mn4cAqIeKoFT7gTeXasqExobpmCiIKM23Fr8TY65vDlVtRw7AfoEyCZASx3E8SUVg
```

* Admin Role
```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MjkxMTAwNzMsImV4cCI6MTY2MDY1MzM0MiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoidGVzdEBnbWFpbC5jb20iLCJyb2xlIjpbInVzZXIiLCJhZG1pbiJdfQ.oAwLchw_VvNLjuV9-sNzqswOMeGBfWBhn7iHHLFyAipg1FS5PLwpTfYOgQzYzE8fX1JvOlwJrw6v0WwyerwiJg
```

### How to run Redis on the command window
```
> redis-server.exe redis.windows.conf
```
### Swagger
* [http://localhost:8080/api-docs/api.html](http://localhost:8080/api-docs/api.html)

### Learned by this project
#### Webflux
1. Mono[ServerResonse] doesn't work in annotation basis (not router function) 
2. There is no benefit to raise NoContext Exception because the response won't has any body content

#### Spring Security
1. Access control in the Controller
   * @PreAuthorize doesn't work in the Controller of WebFlux
   * In SecurityWebFilterChain, you should specify @EnableWebFluxSecurity and pathMachers
```scala
@Configuration
@EnableWebFluxSecurity

class SecurityConfiguration {
  @Bean
  def securityWebFilterChain(http: ServerHttpSecurity,
                             authenticationManager: AuthenticationManager,
                             securityContextRepository: SecurityContextRepository): SecurityWebFilterChain = {
    http      
      ...
      .pathMatchers(HttpMethod.GET, "/usersvc/v1/user/**").hasAnyRole("user", "admin")
      .build()    
  }
}
```
2. Access control in level of method
    * Add @EnableReactiveMethodSecurity above the securityWebFilterChain class
    * You can specify @PreAutorize from the service layer

### HTTP/22
Http2 can be enabled over SSL.

Command to make a self-signed certificate
```
keytool -genkey -alias datasvc -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 4000
```