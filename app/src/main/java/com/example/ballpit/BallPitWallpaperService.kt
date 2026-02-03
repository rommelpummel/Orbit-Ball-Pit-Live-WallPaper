package com.example.ballpit

import android.graphics.*
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlin.random.Random

class BallPitWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = BallPitEngine()

    inner class BallPitEngine : Engine() {

        private val orbs = mutableListOf<Orb>()
        private lateinit var bitmaps: List<Bitmap>
        private var running = true
        private var width = 0
        private var height = 0

        private val GRAVITY = 2000f
        private val FLOOR_RATIO = 0.875f

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            loadBitmaps()
            Thread { loop(holder) }.start()
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            this.width = width
            this.height = height
        }

        private fun loadBitmaps() {
            bitmaps = listOf(
                R.drawable.orb1, R.drawable.orb2, R.drawable.orb3,
                R.drawable.orb4, R.drawable.orb5, R.drawable.orb6,
                R.drawable.orb7, R.drawable.orb8, R.drawable.orb9,
                R.drawable.orb10, R.drawable.orb11
            ).map {
                BitmapFactory.decodeResource(resources, it)
            }
        }

        private fun spawnOrb() {
            val bmp = bitmaps.random()
            val r = bmp.width / 2f
            orbs.add(
                Orb(
                    x = Random.nextFloat() * width,
                    y = -r,
                    vy = 0f,
                    bmp = bmp,
                    r = r
                )
            )
        }

        private fun loop(holder: SurfaceHolder) {
            var last = System.nanoTime()

            while (running) {
                val now = System.nanoTime()
                val dt = (now - last) / 1_000_000_000f
                last = now

                if (Random.nextFloat() < 0.05f) spawnOrb()

                val floor = height * FLOOR_RATIO

                for (o in orbs) {
                    o.vy += GRAVITY * dt
                    o.y += o.vy * dt
                    if (o.y + o.r >= floor) {
                        o.y = floor - o.r
                        o.vy = 0f
                    }
                }

                val c = holder.lockCanvas()
                if (c != null) {
                    c.drawColor(Color.BLACK)
                    for (o in orbs) {
                        c.drawBitmap(o.bmp, o.x - o.r, o.y - o.r, null)
                    }
                    holder.unlockCanvasAndPost(c)
                }

                Thread.sleep(16)
            }
        }
    }

    data class Orb(
        var x: Float,
        var y: Float,
        var vy: Float,
        val bmp: Bitmap,
        val r: Float
    )
}
