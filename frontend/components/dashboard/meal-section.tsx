import { motion } from 'framer-motion'
import { MealCard } from './meal-card'

interface MealSectionProps {
  selectedDate: Date
}

export function MealSection({ selectedDate }: MealSectionProps) {
  const meals = [
    { name: 'Śniadanie' },
    { name: 'II śniadanie' },
    { name: 'Obiad' },
    { name: 'Podwieczorek' },
    { name: 'Kolacja' },
  ]

  return (
    <div className="space-y-6">
      <div className="text-center">
        <h1 className="text-3xl md:text-4xl font-bold bg-gradient-to-r from-emerald-400 to-cyan-400 bg-clip-text text-transparent">
          {selectedDate.toLocaleDateString('pl-PL', {
            weekday: 'long',
            day: 'numeric',
            month: 'long',
          })}
        </h1>
        <p className="mt-1 text-zinc-400">
          Dziś / {selectedDate.toLocaleDateString('pl-PL')}
        </p>
      </div>

      {meals.map((meal, i) => (
        <motion.div
          key={meal.name}
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: i * 0.1 }}
        >
          <MealCard name={meal.name} />
        </motion.div>
      ))}
    </div>
  )
}