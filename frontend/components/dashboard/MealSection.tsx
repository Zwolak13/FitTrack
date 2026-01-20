"use client"

import { motion } from "framer-motion"
import { MealCard } from "./MealCard"
import { useEffect, useState } from "react"
import { useFetchDailyLog } from "@/hooks/dashboard/dailylog/useFetchDailyLog"
import { Meal } from "@/types/meals"
import { AddMealModal } from "./AddMealModal"

interface MealSectionProps {
  selectedDate: Date
}

export function MealSection({ selectedDate }: MealSectionProps) {
  const fetchDailyLog = useFetchDailyLog()
  const [meals, setMeals] = useState<Meal[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [activeMeal, setActiveMeal] = useState<Meal | null>(null)

  useEffect(() => {
    let isMounted = true

    const load = async () => {
      try {
        setLoading(true)
        setError(null)
        const data = await fetchDailyLog(selectedDate.toISOString().slice(0, 10))
        if (!isMounted) return
        setMeals(data.meals)
      } catch (err: any) {
        if (!isMounted) return
        setError(err.message)
      } finally {
        if (!isMounted) return
        setLoading(false)
      }
    }

    load()
    return () => { isMounted = false }
  }, [selectedDate])

  if (loading) return <p>Ładowanie posiłków...</p>
  if (error) return <p className="text-red-500">Błąd: {error}</p>

  return (
    <div className="space-y-6">
      <div className="text-center">
        <h1 className="text-3xl md:text-4xl font-bold bg-gradient-to-r from-emerald-400 to-cyan-400 bg-clip-text text-transparent">
          {selectedDate.toLocaleDateString("pl-PL", {
            weekday: "long",
            day: "numeric",
            month: "long",
          })}
        </h1>
        <p className="mt-1 text-zinc-400">{selectedDate.toLocaleDateString("pl-PL")}</p>
      </div>

      {meals.map((meal, i) => (
        <motion.div
          key={meal.id ?? i}
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: i * 0.1 }}
        >
          <MealCard
            meal={meal}
            onAddMeal={() => setActiveMeal(meal)}
          />
        </motion.div>
      ))}

      {activeMeal && (
        <AddMealModal
          open={!!activeMeal}
          onClose={() => setActiveMeal(null)}
          meal={activeMeal}
          date={selectedDate}
        />
      )}
    </div>
  )
}
