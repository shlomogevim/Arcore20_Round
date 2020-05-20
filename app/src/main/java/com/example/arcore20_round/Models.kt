package com.example.arcore20_round

sealed class Models {

    abstract val degreesPerSecond: Float
    abstract val radius: Float
    abstract val height: Float
    abstract val rotationDegrees: Float

    object Bee : Models() {
        override val degreesPerSecond: Float
            get() = 35f
        override val radius: Float
            get() = 5f
        override val height: Float
            get() = 1.0f
        override val rotationDegrees: Float
            get() = 180f
    }
    object Dance : Models() {
        override val degreesPerSecond: Float
            get() = 20f
        override val radius: Float
            get() = 5f
        override val height: Float
            get() = 1.0f
        override val rotationDegrees: Float
            get() = 180f
    }

    object Fly : Models() {
        override val degreesPerSecond: Float
            get() = 20f
        override val radius: Float
            get() = 2f
        override val height: Float
            get() = 0.7f
        override val rotationDegrees: Float
            get() = 180f
    }
    object Bird : Models() {
        override val degreesPerSecond: Float
            get() = 20f
        override val radius: Float
            get() = 2f
        override val height: Float
            get() = 0.7f
        override val rotationDegrees: Float
            get() = 180f
    }
    object Fish : Models() {
        override val degreesPerSecond: Float
            get() = 25f
        override val radius: Float
            get() = 10f
        override val height: Float
            get() = 0.7f
        override val rotationDegrees: Float
            get() = 180f
    }


    object Boat : Models() {
        override val degreesPerSecond: Float
            get() = 25f
        override val radius: Float
            get() = 5f
        override val height: Float
            get() = 1.2f
        override val rotationDegrees: Float
            get() = 180f
    }
    object Fishka : Models() {
        override val degreesPerSecond: Float
            get() = 15f
        override val radius: Float
            get() = 0.2f
        override val height: Float
            get() = 1.2f
        override val rotationDegrees: Float
            get() = 180f
    }
    object Jet : Models() {
        override val degreesPerSecond: Float
            get() = 20f
        override val radius: Float
            get() = 2f
        override val height: Float
            get() = 0.7f
        override val rotationDegrees: Float
            get() = 180f
    }



}




