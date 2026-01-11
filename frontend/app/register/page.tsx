import LoginForm from "@/components/auth/LoginForm";
import { AuthCard } from "@/components/auth/AuthCard";
import Layout from "@/components/auth/Layout";
import RegisterForm from "@/components/auth/RegisterForm";

export default function LoginPage() {
  return (
    
      <Layout>
        <AuthCard>
            <RegisterForm />
        </AuthCard>
      </Layout>

  );
}
