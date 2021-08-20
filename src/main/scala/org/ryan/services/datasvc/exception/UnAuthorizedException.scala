package org.ryan.services.datasvc.exception

import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.web.server.ResponseStatusException

class UnAuthorizedException(status: HttpStatus = HttpStatus.UNAUTHORIZED,
                            @Nullable reason: String = "Unauthorized",
                            @Nullable cause: Throwable = null)
  extends ResponseStatusException(status, reason, cause) {}
