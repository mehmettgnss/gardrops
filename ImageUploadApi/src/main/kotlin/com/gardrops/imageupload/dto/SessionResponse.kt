package com.gardrops.imageupload.dto

import java.util.UUID

data class SessionResponse @JvmOverloads constructor(
    @JvmField val sessionId: UUID
) 