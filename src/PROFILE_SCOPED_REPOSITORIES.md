# Profile-Scoped Repositories

This document explains how to use the profile-scoped repositories in the MediPal application.

## Overview

The repositories in this application have been updated to be profile-scoped, meaning that all data (medications, appointments, reminders) is now associated with a specific user profile. This allows for multi-user support and data isolation between different profiles.

## Key Changes

### 1. Domain Models
All domain models now include a `profileId` field:
- `Medication.profileId`
- `Appointment.profileId`
- `Reminder.profileId`

### 2. Database Entities
All database entities now include a `profileId` field:
- `MedicationEntity.profileId`
- `AppointmentEntity.profileId`
- `ReminderEntity.profileId`

### 3. DAOs
All DAOs now include profile-scoped queries:
- `getAllByProfileId(profileId: String)`
- `getAllOnceByProfileId(profileId: String)`
- `getByIdAndProfileId(id: String, profileId: String)`
- `deleteByIdAndProfileId(id: String, profileId: String)`

### 4. Repository Implementations
All repository implementations now accept a `profileId` parameter:
- `RoomMedicationRepositoryImpl(dao, profileId)`
- `FirestoreMedicationRepositoryImpl(firestore, profileId)`
- `HybridMedicationRepositoryImpl(localRepo, remoteRepo, networkChecker)`

### 5. Use Cases
Use cases now accept a `profileId` parameter:
- `GetMedicationsUseCase(profileId: String)`
- `AddMedicationUseCase(medication, profileId: String)`
- etc.

## Usage

### 1. Setting the Current Profile

Before using any repositories, set the current profile ID:

```kotlin
val profileRepositoryManager: ProfileRepositoryManager by inject()
profileRepositoryManager.setCurrentProfile("user-profile-id")
```

### 2. Using Profile-Scoped Repositories

#### Option A: Using ProfileRepositoryManager
```kotlin
class MyViewModel(
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {
    
    private val profileId = profileRepositoryManager.getCurrentProfileId()
    private val medicationRepository = profileRepositoryManager.getMedicationRepository()
    
    fun loadMedications() {
        viewModelScope.launch {
            val medications = medicationRepository.getAllOnce()
            // medications will only contain items for the current profile
        }
    }
}
```

#### Option B: Direct Injection with Parameters
```kotlin
class MyViewModel : ViewModel() {
    
    private val medicationRepository: MedicationRepository by inject { 
        parametersOf("user-profile-id") 
    }
    
    fun loadMedications() {
        viewModelScope.launch {
            val medications = medicationRepository.getAllOnce()
            // medications will only contain items for the specified profile
        }
    }
}
```

### 3. Using Use Cases with Profile ID

```kotlin
class MyViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val addMedicationUseCase: AddMedicationUseCase
) : ViewModel() {
    
    private val profileId = "user-profile-id"
    
    fun loadMedications() {
        val medications = getMedicationsUseCase(profileId)
        // medications will only contain items for the specified profile
    }
    
    fun addMedication(medication: Medication) {
        viewModelScope.launch {
            addMedicationUseCase(medication, profileId)
        }
    }
}
```

## Dependency Injection

### Repository Module
The `RepositoryModule` now provides:
- Factory functions for profile-scoped repositories
- Legacy single repositories for backward compatibility

### Profile-Scoped Module
Use `profileScopedModule(profileId)` to create a module with profile-scoped repositories:

```kotlin
val profileModule = profileScopedModule("user-profile-id")
startKoin {
    modules(profileModule)
}
```

## Migration from Legacy Code

### Before (Legacy)
```kotlin
class MyViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase
) : ViewModel() {
    
    fun loadMedications() {
        val medications = getMedicationsUseCase() // No profile filtering
    }
}
```

### After (Profile-Scoped)
```kotlin
class MyViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {
    
    private val profileId = profileRepositoryManager.getCurrentProfileId()
    
    fun loadMedications() {
        val medications = getMedicationsUseCase(profileId) // Profile-scoped
    }
}
```

## Firestore Structure

The Firestore collections are now organized by profile:

```
profiles/
  {profileId}/
    medications/
      {medicationId}/
    appointments/
      {appointmentId}/
    reminders/
      {reminderId}/
```

## Benefits

1. **Multi-user Support**: Multiple users can use the same app instance
2. **Data Isolation**: Each profile's data is completely separate
3. **Security**: Users can only access their own data
4. **Scalability**: Easy to add user management features
5. **Backward Compatibility**: Legacy code still works with default profile

## Notes

- The legacy repositories still exist for backward compatibility
- All new code should use profile-scoped repositories
- The `ProfileRepositoryManager` provides a convenient way to manage the current profile
- Profile switching can be done by calling `setCurrentProfile()` on the manager
