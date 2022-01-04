//
//  TrendingMoviesViewModel.swift
//  iosApp
//
//  Created by feanor on 01/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import shared
import Combine
import Foundation

enum TrendingMoviesState: Equatable {
	case error
	case loading
	case showMovies([MovieTileViewModel])
}

enum TrendingMoviesAction: Equatable {
	case openMovieDetail(Int)
}

struct MovieTileViewModel: Hashable {
	let id: Int
	let title: String
	let posterPath: String
}

private extension MovieTile {
	var asMoviewTileViewModel: MovieTileViewModel {
		MovieTileViewModel(id: Int(id), title: title, posterPath: posterPath)
	}
}

final class TrendingMoviesViewModel: Observer {
	private let trendingMoviesStateMachine: TrendingMoviesStateMachine

	private let actionsSubject: PassthroughSubject<TrendingMoviesAction, Never>
	private let stateSubject: CurrentValueSubject<TrendingMoviesState, Never>

	var actions: AnyPublisher<TrendingMoviesAction, Never> { actionsSubject.removeDuplicates().eraseToAnyPublisher() }
	var state: AnyPublisher<TrendingMoviesState, Never> { stateSubject.removeDuplicates().eraseToAnyPublisher() }

	init(trendingMoviesStateMachine: TrendingMoviesStateMachine) {
		self.trendingMoviesStateMachine = trendingMoviesStateMachine
		self.actionsSubject = PassthroughSubject()
		self.stateSubject = CurrentValueSubject(.loading)
	}

	func bind() {
		trendingMoviesStateMachine.register(observer: self)
	}

	func unbind() {
		trendingMoviesStateMachine.unRegister(observer: self)
	}

	func loadAllMovies() {
		trendingMoviesStateMachine.handleAction(action: shared.TrendingMoviesAction.LoadMovies()) { _, _ in

		}
	}

	func movieClicked(_ id: Int) {
		trendingMoviesStateMachine.handleAction(action: shared.TrendingMoviesAction.MovieClicked(id: Int32(id))) { _, _ in

		}
	}

	// MARK: Observer

	func updateState(state_ state: Any?) {
		switch state {
		case let showMovies as shared.TrendingMoviesState.ShowMovies:
			self.stateSubject.send(.showMovies(showMovies.movies.map({$0.asMoviewTileViewModel})))
		case let navigate as shared.TrendingMoviesState.NavigateToDetails:
			self.actionsSubject.send(.openMovieDetail(Int(navigate.id)))
		case _ as shared.TrendingMoviesState.Error:
			self.stateSubject.send(.error)
		default:
			break
		}

	}

}
