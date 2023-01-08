package bg.nbu.logistics.services.income;

import java.time.LocalDate;

public interface IncomeService {
    double getIncomeByTimePeriod(LocalDate from, LocalDate to);
}
