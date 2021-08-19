package org.ryan.services.datasvc.exception

import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.web.server.ResponseStatusException

class BadRequestException(status: HttpStatus = HttpStatus.BAD_REQUEST, @Nullable reason: String = "Bad Request")
  extends ResponseStatusException(status, reason) {}