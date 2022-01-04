//
//  StateMachineAssembly.swift
//  iosApp
//
//  Created by feanor on 01/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import shared
import Swinject
import Foundation

final class StateMachineAssembly: Assembly {

	func assemble(container: Container) {
		container.register(TrendingMoviesStateMachine.self) { resolver in
			TrendingMoviesStateMachine(trendingDataSource: resolver.resolve(TrendingDataSource.self)!)
		}

		container.register(MovieDetailsStateMachine.self) { resolver in
			MovieDetailsStateMachine(dataSource: resolver.resolve(MovieDetailsDataSource.self)!)
		}
	}

}
