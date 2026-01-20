"use client"

import { useState } from "react"
import { Check, Plus } from "lucide-react"
import { useFetchIngredients } from "@/hooks/dashboard/ingredients/useFetchIngredients"

interface IngredientListProps {
  type: "all" | "mine"
}

export function IngredientList({ type }: IngredientListProps) {
  const [inputValue, setInputValue] = useState("")
  const [query, setQuery] = useState("")
  const { ingredients, loading, error } = useFetchIngredients(type, query)
  const [selected, setSelected] = useState<number[]>([])

  const toggle = (id: number) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    )
  }

  return (
    <div className="flex flex-col gap-3 h-full">
      <input
        value={inputValue}
        onChange={(e) => setInputValue(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") setQuery(inputValue)
        }}
        placeholder="Szukaj składnika..."
        className="rounded-lg border border-white/10 bg-black px-3 py-2 text-sm text-white outline-none focus:border-emerald-500"
      />

      {loading && <p className="text-zinc-400">Ładowanie…</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && ingredients.length === 0 && (
        <p className="text-zinc-500 w-full text-center">Brak składników</p>
      )}

      <div className="space-y-2 overflow-y-auto flex-1">
        {ingredients.map((i) => (
          <button
            key={i.id}
            onClick={() => toggle(i.id)}
            className={`flex w-full items-center justify-between rounded-lg border border-white/10 px-3 py-2 text-sm text-zinc-300 hover:border-emerald-500/40 ${
              selected.includes(i.id) ? "bg-emerald-950/30" : ""
            }`}
          >
            <span>{i.name}</span>
            {selected.includes(i.id) ? (
              <Check className="h-4 w-4 text-emerald-400" />
            ) : (
              <Plus className="h-4 w-4 text-zinc-500" />
            )}
          </button>
        ))}
      </div>
    </div>
  )
}
