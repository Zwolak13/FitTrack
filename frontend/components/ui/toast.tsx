'use client'

import { motion } from 'framer-motion'
import { X } from 'lucide-react'
import { cn } from '@/lib/utils'
import { ToastProps } from '@/types/toast'

const variantStyles: Record<NonNullable<ToastProps['variant']>, string> = {
  success: 'border-emerald-700/60 bg-gradient-to-r from-emerald-950/90 to-emerald-900/90 text-emerald-50',
  error:   'border-rose-700/60 bg-gradient-to-r from-rose-950/90 to-red-950/90 text-rose-50',
  warning: 'border-amber-700/60 bg-gradient-to-r from-amber-950/90 to-orange-950/90 text-amber-50',
  info:    'border-cyan-700/60 bg-gradient-to-r from-cyan-950/90 to-blue-950/90 text-cyan-50',
  default: 'border-zinc-700/60 bg-zinc-950/90 text-zinc-100 backdrop-blur-xl',
}

const barColors: Record<NonNullable<ToastProps['variant']>, string> = {
  success: 'bg-emerald-500',
  error:   'bg-rose-500',
  warning: 'bg-amber-500',
  info:    'bg-cyan-500',
  default: 'bg-zinc-500',
}

export function Toast({
  toast,
  onRemove,
}: {
  toast: ToastProps
  onRemove: () => void
}) {
  const { title, description, variant = 'default', duration = 5000 } = toast

  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: 24, scale: 0.95 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      exit={{ opacity: 0, y: 12, scale: 0.96 }}
      transition={{ type: 'spring', stiffness: 300, damping: 28 }}
      className={cn(
        'pointer-events-auto relative flex w-full max-w-md items-start gap-3.5 rounded-xl border p-4 shadow-2xl backdrop-blur-xl',
        variantStyles[variant]
      )}
    >
      <div className={cn('mt-0.5 h-9 w-1.5 shrink-0 rounded-full', barColors[variant])} />

      <div className="flex-1 space-y-1">
        {title && <p className="font-medium leading-tight">{title}</p>}
        {description && (
          <p className={cn('text-sm opacity-90', !title && 'pt-0.5')}>
            {description}
          </p>
        )}
      </div>

      <button
        onClick={onRemove}
        className="mt-0.5 rounded-full p-1.5 opacity-60 transition-opacity hover:opacity-100"
        aria-label="Zamknij powiadomienie"
      >
        <X size={16} />
      </button>
    </motion.div>
  )
}