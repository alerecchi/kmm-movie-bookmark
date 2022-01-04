//
//  ViewModelAssembly.swift
//  iosApp
//
//  Created by feanor on 01/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import shared
import Swinject
import Foundation

final class ViewModelAssembly: Assembly {
	func assemble(container: Container) {
		container.register(TrendingMoviesViewModel.self) { resolver in
			TrendingMoviesViewModel(trendingMoviesStateMachine: resolver.resolve(TrendingMoviesStateMachine.self)!)
		}

		container.register(MovieDetailViewModel.self) { resolver in
			MovieDetailViewModel(stateMachine: resolver.resolve(MovieDetailsStateMachine.self)!)
		}
	}
}
