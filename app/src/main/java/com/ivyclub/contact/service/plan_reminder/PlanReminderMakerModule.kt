package com.ivyclub.contact.service.plan_reminder

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface PlanReminderMakerModule {
    @Binds
    fun providePlanReminderMakerImpl(planReminderMaker: PlanReminderMakerImpl): PlanReminderMaker
}