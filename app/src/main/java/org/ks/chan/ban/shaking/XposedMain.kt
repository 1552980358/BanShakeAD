package org.ks.chan.ban.shaking

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.Keep
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import java.lang.reflect.Method

@Keep
class XposedMain(
    base: XposedInterface,
    param: XposedModuleInterface.ModuleLoadedParam
): XposedModule(base, param) {

    @XposedHooker
    private object SensorListenerRegisterHooker: XposedInterface.Hooker {

        private const val SensorManagerRegisterListener = "registerListener"
        private fun methodFilter(method: Method): Boolean {
            return method.name == SensorManagerRegisterListener
        }

        private val methods: List<Method>
            get() = SensorManager::class.java
                .declaredMethods
                .filter(::methodFilter)

        private val sensorTypes = intArrayOf(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_SIGNIFICANT_MOTION,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_ROTATION_VECTOR,
        )

        @Keep
        @JvmStatic
        @BeforeInvocation
        fun before(callback: XposedInterface.BeforeHookCallback) {
            handleMethod(callback = callback)
        }

        private fun handleMethod(
            callback: XposedInterface.BeforeHookCallback,
            args: Array<Any> = callback.args,
        ) {
            if (args.size >= 2) {
                checkAndSkip(
                    callback = callback,
                    sensor = args[1] as? Sensor ?: return,
                )
            }
        }

        private fun checkAndSkip(
            callback: XposedInterface.BeforeHookCallback,
            sensor: Sensor,
        ) {
            if (sensor.type in sensorTypes) {
                /**
                 * [SensorManager.registerListener]
                 * @return true if the sensor is supported and successfully enabled
                 **/
                // Make it not supported
                callback.returnAndSkip(false)
            }
        }

        private val HookerClass: Class<SensorListenerRegisterHooker>
            get() = SensorListenerRegisterHooker::class.java

        fun hook(
            xposedModule: XposedModule,
            hookerClass: Class<SensorListenerRegisterHooker> = HookerClass
        ) {
            methods.forEach { method ->
                xposedModule.hook(method, hookerClass)
            }
        }

    }

    init {
        log("Module initializes at ${param.processName}")
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        super.onPackageLoaded(param)
        log("Module hooks ${param.packageName}")
        log("Class Loader = ${param.classLoader}")
        SensorListenerRegisterHooker.hook(this)
    }

}