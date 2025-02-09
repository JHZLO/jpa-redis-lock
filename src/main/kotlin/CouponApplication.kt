package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class CouponApplication
fun main(args:Array<String>) {
    runApplication<CouponApplication>(*args)
}
