//
//  DataSourceAssembly.swift
//  iosApp
//
//  Created by feanor on 01/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import shared
import Swinject
import Foundation

final class DataSourceAssembly: Assembly {
	func assemble(container: Container) {
		container.register(TrendingDataSource.self) { _ in
			NetworkModule().provideTrendingDataSource()
		}

		container.register(MovieDetailsDataSource.self) { _ in
			NetworkModule().provideMovieDetailsDataSource()
		}
	}

}
