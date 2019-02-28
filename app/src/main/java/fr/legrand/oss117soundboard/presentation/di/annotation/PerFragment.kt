package fr.legrand.oss117soundboard.presentation.di.annotation

import javax.inject.Scope

/**
 * Scope annotation for Fragment scope
 * This annotation is only used on scoped object with constructor injection
 * We do not annotate objects which are provided/bound in a Dagger Module
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment