package org.ryan.services.datasvc.exception

import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.web.server.ResponseStatusException

class NoContentException(status: HttpStatus = HttpStatus.NO_CONTENT,
                         @Nullable reason: String = "No Content",
                         @Nullable cause: Throwable = null)
  extends ResponseStatusException(status, reason, cause) {}
