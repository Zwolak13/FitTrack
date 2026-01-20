"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { useAddCustomIngredient, NewIngredient } from "@/hooks/dashboard/ingredients/useAddCustomIngredient"
import { useToast } from "../ToastProvider"
import { Meal } from "@/types/meals"

interface AddIngredientFormProps {
  meal: Meal
  date: Date,
  onAdded?: () => void
}

export function AddIngredientForm({ meal,date, onAdded }: AddIngredientFormProps) {
  const { addCustomIngredient, loading } = useAddCustomIngredient(meal.id)
  const { toast } = useToast()
  const [name, setName] = useState("")
  const [calories, setCalories] = useState("")
  const [protein, setProtein] = useState("")
  const [carbs, setCarbs] = useState("")
  const [fat, setFat] = useState("")

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!name.trim()) {
      toast({
        title: "Błąd",
        description: "Nazwa składnika nie może być pusta",
        variant: "error",
      })
      return
    }

    const ingredient: NewIngredient = {
        name,
        calories: Number(calories),
        protein: Number(protein),
        carbs: Number(carbs),
        fat: Number(fat),
        mealType: meal.type, 
        date: date
}

    try {
      await addCustomIngredient(ingredient)

      setName("")
      setCalories("")
      setProtein("")
      setCarbs("")
      setFat("")

      toast({
        title: "Sukces",
        description: `Dodano składnik ${ingredient.name}`,
        variant: "success",
      })

      if (onAdded) onAdded()
    } catch (err: any) {
      toast({
        title: "Błąd",
        description: err?.message ?? "Nieznany błąd",
        variant: "error",
      })
    }
  }

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
      <input
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="Nazwa składnika"
        className="w-full rounded-md bg-black border border-white/10 px-3 py-2 text-sm text-zinc-200"
      />
      <div className="grid grid-cols-2 gap-2">
        <input
          value={calories}
          onChange={(e) => setCalories(e.target.value)}
          placeholder="Kalorie"
          type="number"
          className="w-full rounded-md bg-black border border-white/10 px-3 py-2 text-sm text-zinc-200"
        />
        <input
          value={protein}
          onChange={(e) => setProtein(e.target.value)}
          placeholder="Białko"
          type="number"
          className="w-full rounded-md bg-black border border-white/10 px-3 py-2 text-sm text-zinc-200"
        />
        <input
          value={carbs}
          onChange={(e) => setCarbs(e.target.value)}
          placeholder="Węglowodany"
          type="number"
          className="w-full rounded-md bg-black border border-white/10 px-3 py-2 text-sm text-zinc-200"
        />
        <input
          value={fat}
          onChange={(e) => setFat(e.target.value)}
          placeholder="Tłuszcz"
          type="number"
          className="w-full rounded-md bg-black border border-white/10 px-3 py-2 text-sm text-zinc-200"
        />
      </div>
      <Button
        type="submit"
        className="w-full bg-emerald-600 hover:bg-emerald-500"
        disabled={loading}
      >
        {loading ? "Dodawanie..." : "Dodaj"}
      </Button>
    </form>
  )
}
