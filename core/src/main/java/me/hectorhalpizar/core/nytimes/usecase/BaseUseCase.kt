package me.hectorhalpizar.core.nytimes.usecase

interface BaseUseCase<Input, Output> {
    operator fun invoke(input: Input) : Output
}