import { useState } from "react";
import { ProfessionalStats } from "@/components/ProfessionalStats/ProfessionalStats";
import { useProfessionalStats } from "@/services/StatsService";
import { StatsPeriod } from "@/models/Stats";
import { ProfessionalLayout } from "@/components/ProfessionalLayout/ProfessionalLayout";

export const ProfessionalStatsScreen = () => {
  const [period, setPeriod] = useState<StatsPeriod>("30d");
  const { data, isLoading } = useProfessionalStats(period);

  return (
    <ProfessionalLayout>
      <ProfessionalStats
        stats={data}
        isLoading={isLoading}
        period={period}
        onPeriodChange={setPeriod}
      />
    </ProfessionalLayout>
  );
};