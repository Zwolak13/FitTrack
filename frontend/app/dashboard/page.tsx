'use client'

import { useState } from 'react'
import { DateScroller } from '@/components/dashboard/date-scroller'
import { MealSection } from '@/components/dashboard/meal-section'
import { DrawerMenu } from '@/components/dashboard/drawer-menu'
import { Menu } from 'lucide-react'

export default function Dashboard() {
  const [selectedDate, setSelectedDate] = useState(new Date())
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)

  return (
    <div className="min-h-screen bg-gradient-to-b from-black to-zinc-950 text-white">
      <button
        onClick={() => setIsDrawerOpen(true)}
        className="fixed top-4 right-4 z-50 p-2 bg-black/40 backdrop-blur-lg border border-white/10 rounded-full hover:bg-white/10"
      >
        <Menu className="h-5 w-5" />
      </button>

      <div className="container max-w-5xl mx-auto px-4 py-8 md:py-12">
        <DateScroller
          selectedDate={selectedDate}
          onDateChange={setSelectedDate}
        />

        <MealSection selectedDate={selectedDate} />
      </div>

      <DrawerMenu
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
      />
    </div>
  )
}