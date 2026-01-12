"use client";

import { motion, AnimatePresence } from "framer-motion";

export function LoadingOverlay({ visible }: { visible: boolean }) {
  return (
    <AnimatePresence>
      {visible && (
        <motion.div
          key="loading-overlay"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="fixed inset-0 z-[9999] flex items-center justify-center bg-black backdrop-blur-sm"
        >
          <motion.div
            className="w-16 h-16 border-4 border-t-emerald-400 border-b-emerald-400 border-l-transparent border-r-transparent rounded-full animate-spin"
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            exit={{ scale: 0 }}
            transition={{ type: "spring", stiffness: 300, damping: 20 }}
          />
        </motion.div>
      )}
    </AnimatePresence>
  );
}
