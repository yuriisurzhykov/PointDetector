package com.yuriisurzhykov.foodbanks.core

import kotlin.reflect.KClass

class MappingException(input: Any, toClazz: KClass<*>) :
    Exception("Cannot map $input to $toClazz class!")