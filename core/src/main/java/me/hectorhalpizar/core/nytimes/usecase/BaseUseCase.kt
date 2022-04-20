package me.hectorhalpizar.core.nytimes.usecase

interface BaseUseCase<Input, Output> {
    suspend operator fun invoke(input: Input) : Output
    sealed class Error(message: String?, cause: Throwable?) : Exception(message, cause)
}