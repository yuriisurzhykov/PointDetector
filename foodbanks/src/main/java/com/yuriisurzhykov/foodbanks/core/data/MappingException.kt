package com.yuriisurzhykov.foodbanks.core.data

import kotlin.reflect.KClass

class MappingException(input: Any, toClazz: KClass<*>) :
    Exception("Cannot map $input to $toClazz class!")