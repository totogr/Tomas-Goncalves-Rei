import { Route, Switch } from "wouter";
import { RoleProvider } from "@/services/RoleProvider";
import { RoleSelectionScreen } from "@/screens/RoleSelectionScreen";
import { LoginScreen } from "@/screens/LoginScreen";
import { OnboardingServiceScreen } from "@/screens/OnboardingServiceScreen.tsx";
import { ServicesConfigScreen } from "@/screens/ServicesConfigScreen";
import { ProfessionalScreen } from "@/screens/ProfessionalScreen";
import { ProfessionalListScreen } from "@/screens/ProfessionalListScreen";
import { ClientSignupScreen } from "@/screens/ClientSignupScreen";
import { ProfessionalSignupScreen } from "@/screens/ProfessionalSignupScreen";
import { WorkingHoursConfigScreen } from "@/screens/WorkingHoursConfigScreen";
import { BlockedHoursConfigScreen } from "@/screens/BlockedHoursConfigScreen";
import { BlockedClientsConfigScreen } from "@/screens/BlockedClientsConfigScreen";
import { OnboardingHorariosScreen } from "@/screens/OnboardingScheduleScreen";
import { OnboardingPerfilScreen } from "@/screens/OnboardingPerfilScreen.tsx";
import { ForgotPasswordScreen } from "@/screens/ForgotPasswordScreen";
import { ResetPasswordScreen } from "@/screens/ResetPasswordScreen";
import { ClientBookingsScreen } from "@/screens/ClientBookingsScreen";
import { BookingDetailScreen } from "@/screens/BookingDetailScreen";
import { ProfessionalAgendaScreen } from "@/screens/ProfessionalAgendaScreen";
import { ProfessionalStatsScreen } from "./screens/ProfessionalStatsScreen";

export const Navigation = () => (
  <RoleProvider>
    <Switch>
      <Route path="/" component={RoleSelectionScreen} />
      <Route path="/login" component={LoginScreen} />

      <Route path="/signup/client" component={ClientSignupScreen} />
      <Route path="/signup/professional" component={ProfessionalSignupScreen} />

      <Route path="/forgot-password" component={ForgotPasswordScreen} />
      <Route path="/reset-password" component={ResetPasswordScreen} />

      <Route path="/professional/onboarding/perfil" component={OnboardingPerfilScreen} />
      <Route path="/professional/onboarding/servicios" component={OnboardingServiceScreen} />
      <Route path="/professional/onboarding/horarios" component={OnboardingHorariosScreen} />
      <Route path="/professional/agenda" component={ProfessionalAgendaScreen} />

      <Route path="/professionals" component={ProfessionalListScreen} />
      <Route path="/professionals/:id">
        {(params) => <ProfessionalScreen id={params.id} />}
      </Route>

      <Route path="/professional/stats" component={ProfessionalStatsScreen} />

      <Route path="/professional/config/servicios" component={ServicesConfigScreen} />
      <Route path="/professional/config/horarios" component={WorkingHoursConfigScreen} />
      <Route path="/professional/config/bloqueo-horarios" component={BlockedHoursConfigScreen} />
      <Route path="/professional/config/bloqueo-clientes" component={BlockedClientsConfigScreen} />

      <Route path="/bookings/me" component={ClientBookingsScreen} />
      <Route path="/bookings/:id" component={BookingDetailScreen} />
    </Switch>
  </RoleProvider>
);