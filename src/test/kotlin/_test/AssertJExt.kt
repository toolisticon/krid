package io.toolisticon.lib.krid._test

import org.assertj.core.api.AbstractThrowableAssert
import kotlin.reflect.KClass

fun AbstractThrowableAssert<*, out Throwable>.isInstanceOf(kclass:KClass<*>) = isInstanceOf(kclass.java)
