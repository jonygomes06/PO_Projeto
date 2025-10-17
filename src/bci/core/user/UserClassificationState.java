package bci.core.user;

import bci.core.work.Work;

interface UserClassificationState {
    UserClassificationState updateStateOnDateChange(User user, int currentDate);
    int getMaxSimultaneousRequests();
    int getMaxWorkRequestPrice();
    int getRequestDuration(Work work);
}
