"use client";

import { useState } from "react";
import { motion } from "framer-motion";
import { Eye, EyeOff, Lock, Mail } from "lucide-react";
import Link from 'next/link'
import {useLogin} from "@/hooks/auth";
import { useToast } from "../ToastProvider";

export default function LoginForm() {
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const login = useLogin();

  const { success, error } = useToast();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      await login(email, password);
    } catch (err) {
      error({ title: "Błąd!", description: "Nie udało się zalogować." });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative w-full max-w-md px-5 py-10 md:p-0">
      <div className="absolute inset-0 -z-10 rounded-3xl bg-gradient-to-br from-black via-black/95 to-black/80" />
      <div className="absolute inset-0 -z-10 rounded-3xl bg-[radial-gradient(ellipse_at_top_right,_rgba(52,211,153,0.12),transparent_40%)]" />
      <div className="absolute inset-0 -z-10 rounded-3xl bg-[radial-gradient(ellipse_at_bottom_left,_rgba(34,211,238,0.11),transparent_40%)]" />

      <div 
        className={`
          relative overflow-hidden rounded-3xl 
          border border-white/10 
          bg-black/40 backdrop-blur-2xl 
          shadow-2xl shadow-black/60
          transition-all duration-500
        `}
      >
        <div className="absolute -top-24 left-1/2 h-48 w-96 -translate-x-1/2 bg-gradient-to-b from-emerald-500/20 to-transparent blur-3xl" />
        <div className="absolute -top-32 left-1/2 h-64 w-80 -translate-x-1/2 bg-gradient-to-b from-cyan-500/15 to-transparent blur-3xl" />

        <div className="p-8 md:p-10 space-y-8">
          <div className="text-center space-y-3">
            <motion.h1
              initial={{ y: -20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ duration: 0.7 }}
              className="text-4xl md:text-5xl font-black tracking-tight"
            >
              <span className="bg-gradient-to-r from-emerald-400 via-teal-400 to-cyan-400 bg-clip-text text-transparent">
                FitTrack
              </span>
            </motion.h1>
            
            <motion.p
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.2, duration: 0.8 }}
              className="text-white/60 text-lg"
            >
              Wróć do gry i bij rekordy 💪
            </motion.p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">

            <motion.div
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.3 }}
              className="relative group"
            >
              <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                <Mail className="h-5 w-5 text-white/40 group-focus-within:text-emerald-400 transition-colors" />
              </div>
              
              <input
                type="email"
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="Email"
                className={`
                  w-full pl-11 pr-4 py-4 rounded-2xl
                  bg-white/5 border border-white/10
                  text-white placeholder:text-white/40
                  focus:outline-none focus:border-emerald-500/60
                  focus:bg-white/10 focus:shadow-lg focus:shadow-emerald-500/10
                  transition-all duration-300
                `}
              />
            </motion.div>

            <motion.div
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.4 }}
              className="relative group"
            >
              <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                <Lock className="h-5 w-5 text-white/40 group-focus-within:text-cyan-400 transition-colors" />
              </div>

              <input
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="Hasło"
                className={`
                  w-full pl-11 pr-12 py-4 rounded-2xl
                  bg-white/5 border border-white/10
                  text-white placeholder:text-white/40
                  focus:outline-none focus:border-cyan-500/60
                  focus:bg-white/10 focus:shadow-lg focus:shadow-cyan-500/10
                  transition-all duration-300
                `}
              />

              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute inset-y-0 right-0 pr-4 flex items-center text-white/50 hover:text-white transition-colors"
              >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </motion.div>

            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              disabled={isLoading}
              type="submit"
              className={`
                relative w-full py-4.5 rounded-2xl font-semibold text-lg
                overflow-hidden group
                bg-gradient-to-r from-emerald-600 via-teal-500 to-cyan-600
                shadow-lg shadow-emerald-900/30
                transition-all duration-300
                disabled:opacity-70
              `}
            >
              <div className="absolute inset-0 bg-gradient-to-r from-emerald-400/30 to-cyan-400/30 opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
              
              <span className="relative">
                {isLoading ? "Logowanie..." : "Zaloguj się"}
              </span>
            </motion.button>
          </form>

          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.6 }}
            className="flex justify-end text-sm text-white/60"
          >
            <Link href="/register" className="hover:text-cyan-400 transition-colors">
              Utwórz konto →
            </Link>
          </motion.div>
        </div>
      </div>
    </div>
  );
}