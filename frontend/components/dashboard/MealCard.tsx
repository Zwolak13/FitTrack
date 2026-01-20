"use client"

import { Plus } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Meal } from "@/types/meals"

interface MealCardProps {
  meal: Meal
  onAddMeal: () => void
}

const MEAL_TYPE_LABELS: Record<Meal["type"], string> = {
  BREAKFAST: "Śniadanie",
  LUNCH: "Lunch",
  DINNER: "Obiad",
  SUPPER: "Kolacja",
  SNACK: "Przekąska",
}

export function MealCard({ meal, onAddMeal }: MealCardProps) {
  const ingredients = meal.ingredients ?? []

  return (
    <Card className="overflow-hidden border-white/10 bg-black/40 backdrop-blur-xl transition-all hover:border-emerald-500/30">
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className="text-xl font-semibold text-emerald-400">
          {MEAL_TYPE_LABELS[meal.type]}
        </CardTitle>
        <Button
          size="icon"
          variant="ghost"
          className="h-8 w-8 rounded-full text-emerald-400 hover:bg-emerald-950/50 hover:text-emerald-300 cursor-pointer"
          onClick={onAddMeal}
        >
          <Plus className="h-4 w-4" />
        </Button>
      </CardHeader>
      <CardContent className="space-y-2">
        {ingredients.length === 0 ? (
          <div className="flex items-center justify-between text-sm text-zinc-400">
            <span>Brak dodanych produktów</span>
          </div>
        ) : (
          ingredients.map((mi) => (
            <div
              key={mi.id}
              className="flex items-center justify-between text-sm text-zinc-400"
            >
              <span>
                {mi.ingredient.name} ({mi.quantity})
              </span>
              <span className="font-medium text-emerald-400">
                {meal.user !== null &&
                <>
                  <span>
                  {mi.ingredient.caloriesPer100g * mi.quantity} kcal 
                </span>
                {" "}
                  <span className="text-orange-600">
                    {mi.ingredient.carbsPer100g * mi.quantity}gW
                  </span>
                  {" "}
                  <span className="text-white">
                    {mi.ingredient.proteinPer100g * mi.quantity}gB
                  </span>

                  {" "}
                  <span className="text-yellow-200">
                    {mi.ingredient.fatPer100g * mi.quantity}gT
                  </span>
                </>
            
                }
                
                
              </span>
            </div>
          ))
        )}
        <hr className="border-zinc-700" />
        <div className="flex items-center justify-between text-sm font-medium text-zinc-300">
          <span>Podsumowanie:</span>
          <div className="flex space-x-4">
            <span>{meal.calories} kcal</span>
            <span>{meal.protein} g B</span>
            <span>{meal.carbs} g W</span>
            <span>{meal.fat} g T</span>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
