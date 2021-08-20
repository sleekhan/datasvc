package org.ryan.services.datasvc.exception

import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.web.server.ResponseStatusException

class ForbiddenException(status: HttpStatus = HttpStatus.FORBIDDEN,
                         @Nullable reason: String = "Forbidden",
                         @Nullable cause: Throwable = null)
  extends ResponseStatusException(status, reason, cause) {}
