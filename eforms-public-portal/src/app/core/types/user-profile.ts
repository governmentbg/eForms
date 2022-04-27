export interface UserProfile {
    personName: string,
    personIdentifier: string
}

export interface UserProfileResponse {
    _id: string,
    data: UserProfile
}
