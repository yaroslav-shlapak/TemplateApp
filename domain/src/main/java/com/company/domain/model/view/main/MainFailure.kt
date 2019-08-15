package com.company.domain.model.view.main

import com.company.domain.model.Failure

/**
 * Created on 2019/02/04.
 */
sealed class MainFailure : Failure.FeatureFailure() {
    object SomeError : MainFailure()
}