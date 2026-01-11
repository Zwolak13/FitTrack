import { Plus } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

interface MealCardProps {
  name: string
  calories?: number
}

export function MealCard({ name, calories = 0 }: MealCardProps) {
  return (
    <Card className="overflow-hidden border-white/10 bg-black/40 backdrop-blur-xl transition-all hover:border-emerald-500/30">
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-xl font-semibold">{name}</CardTitle>
        <Button size="icon" variant="ghost" className="h-8 w-8 rounded-full hover:bg-emerald-950/50">
          <Plus className="h-4 w-4" />
        </Button>
      </CardHeader>
      <CardContent>
        <div className="flex items-center justify-between text-sm text-zinc-400">
          <span>Brak dodanych produktów</span>
          <span className="font-medium text-emerald-400">{calories} kcal</span>
        </div>
      </CardContent>
    </Card>
  )
}