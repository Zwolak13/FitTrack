'use client'

import { useEffect, useRef, useMemo, useState, useLayoutEffect } from 'react'
import { motion, animate } from 'framer-motion'
import { cn } from '@/lib/utils'

interface DateScrollerProps {
  selectedDate: Date
  onDateChange: (date: Date) => void
}

export function DateScroller({ selectedDate, onDateChange }: DateScrollerProps) {
  const scrollRef = useRef<HTMLDivElement>(null)
  const [itemWidth, setItemWidth] = useState(0)
  const [gap, setGap] = useState(0)
  const [paddingLeft, setPaddingLeft] = useState(0)
  const isDraggingRef = useRef(false)

  const days = useMemo(() => {
    const today = new Date()
    const start = new Date(today)
    start.setDate(today.getDate() - 30)

    return Array.from({ length: 61 }, (_, i) => {
      const d = new Date(start)
      d.setDate(start.getDate() + i)
      return d
    })
  }, [])

  const isMounted = useRef(false)
  useEffect(() => {
    if (isMounted.current) return
    isMounted.current = true

    const today = new Date()
    if (!selectedDate || isNaN(selectedDate.getTime())) {
      onDateChange(today)
    }
  }, [onDateChange, selectedDate])

  useLayoutEffect(() => {
    if (!scrollRef.current?.firstElementChild) return

    const style = getComputedStyle(scrollRef.current)
    setItemWidth(scrollRef.current.firstElementChild.getBoundingClientRect().width)
    setGap(parseFloat(style.columnGap) || 0)
    setPaddingLeft(parseFloat(style.paddingLeft) || 0)
  }, [days])

  useLayoutEffect(() => {
    if (!scrollRef.current || itemWidth === 0 || gap === 0) return

    const todayIdx = days.findIndex(d => d.toDateString() === new Date().toDateString())
    if (todayIdx < 0) return

    const itemStride = itemWidth + gap
    const containerWidth = scrollRef.current.clientWidth
    const target = paddingLeft + todayIdx * itemStride + itemWidth / 2 - containerWidth / 2
    scrollRef.current.scrollLeft = target
  }, [days, itemWidth, gap, paddingLeft])

  useEffect(() => {
    if (!scrollRef.current || itemWidth === 0 || gap === 0) return

    const idx = days.findIndex(d => d.toDateString() === selectedDate.toDateString())
    if (idx < 0) return

    const itemStride = itemWidth + gap
    const containerWidth = scrollRef.current.clientWidth
    const target = paddingLeft + idx * itemStride + itemWidth / 2 - containerWidth / 2

    animate(scrollRef.current.scrollLeft, target, {
      type: 'spring',
      stiffness: 150,
      damping: 25,
      onUpdate: (v) => {
        if (scrollRef.current) scrollRef.current.scrollLeft = v
      }
    })
  }, [selectedDate, days, itemWidth, gap, paddingLeft])

  useEffect(() => {
    const el = scrollRef.current
    if (!el || itemWidth === 0 || gap === 0) return

    const itemStride = itemWidth + gap

    let startX = 0
    let scrollStart = 0

    const onDown = (e: PointerEvent) => {
      isDraggingRef.current = true
      startX = e.pageX
      scrollStart = el.scrollLeft
      el.setPointerCapture(e.pointerId)
      e.preventDefault()
    }

    const onMove = (e: PointerEvent) => {
      if (!isDraggingRef.current) return
      e.preventDefault()
      const delta = e.pageX - startX
      el.scrollLeft = scrollStart - delta * 1.8
    }

    const onUp = () => {
      if (!isDraggingRef.current) return
      isDraggingRef.current = false

      const containerWidth = el.clientWidth
      const currentCenterPos = el.scrollLeft + containerWidth / 2
      const adjustedPos = currentCenterPos - paddingLeft - itemWidth / 2
      const targetIdx = Math.round(adjustedPos / itemStride)
      const clamped = Math.max(0, Math.min(targetIdx, days.length - 1))

      const targetScroll = paddingLeft + clamped * itemStride + itemWidth / 2 - containerWidth / 2

      animate(el.scrollLeft, targetScroll, {
        type: 'spring',
        stiffness: 180,
        damping: 26,
        onUpdate: (v) => {
          el.scrollLeft = v
        },
        onComplete: () => {
          onDateChange(days[clamped])
        }
      })
    }

    el.addEventListener('pointerdown', onDown)
    window.addEventListener('pointermove', onMove)
    window.addEventListener('pointerup', onUp)
    window.addEventListener('pointercancel', onUp)

    return () => {
      el.removeEventListener('pointerdown', onDown)
      window.removeEventListener('pointermove', onMove)
      window.removeEventListener('pointerup', onUp)
      window.removeEventListener('pointercancel', onUp)
    }
  }, [days, itemWidth, gap, paddingLeft, onDateChange])

  return (
    <div className="relative mb-10">
      <div className="w-full rounded-xl border border-white/10 bg-black/40 backdrop-blur-xl overflow-hidden">
        <div
          ref={scrollRef}
          className="
            flex items-center gap-6 px-12 py-6
            overflow-x-auto overscroll-x-contain
            snap-x snap-mandatory
            scrollbar-hide
            select-none touch-pan-x
            cursor-grab active:cursor-grabbing
          "
        >
          {days.map((day) => {
            const isSelected = day.toDateString() === selectedDate.toDateString()
            const isToday = day.toDateString() === new Date().toDateString()

            return (
              <motion.button
                key={day.toISOString()}
                whileTap={{ scale: 0.92 }}
                onClick={(e) => {
                  e.stopPropagation()
                  onDateChange(new Date(day))
                }}
                className={cn(
                  "flex min-w-[76px] shrink-0 flex-col items-center justify-center rounded-lg px-3 py-4 text-sm transition-all snap-center pointer-events-auto",
                  isSelected
                    ? "bg-gradient-to-b from-emerald-600 to-emerald-800 shadow-lg shadow-emerald-900/50 text-white font-medium"
                    : isToday
                    ? "bg-white/10 border border-emerald-500/50 text-emerald-300"
                    : "hover:bg-white/5 text-zinc-400"
                )}
              >
                <span className="text-xs opacity-80">
                  {day.toLocaleDateString('pl-PL', { weekday: 'short' })}
                </span>
                <span className="text-2xl font-bold">{day.getDate()}</span>
                {isToday && <span className="mt-2 h-2.5 w-2.5 rounded-full bg-emerald-400" />}
              </motion.button>
            )
          })}
        </div>
      </div>
    </div>
  )
}