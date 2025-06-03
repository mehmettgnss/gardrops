package com.gardrops.imageupload.dto

import java.util.UUID

data class ImagesListResponse @JvmOverloads constructor(
    @JvmField val imageIds: List<UUID>
) 