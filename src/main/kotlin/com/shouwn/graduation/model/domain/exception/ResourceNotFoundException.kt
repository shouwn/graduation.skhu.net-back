package com.shouwn.graduation.model.domain.exception

class ResourceNotFoundException
constructor(
        var resourceName: String,
        var fieldName: String,
        var fieldValue: Any
): RuntimeException(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue))