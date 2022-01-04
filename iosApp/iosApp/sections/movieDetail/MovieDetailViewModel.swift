//
//  MovieDetailViewModel.swift
//  iosApp
//
//  Created by feanor on 02/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import shared
import Combine
import Foundation

struct CastMemberViewModel: Hashable {
    let name: String
    let profilePath: String?
}

struct MovieDetailsViewModel: Hashable {
	let overview: String?
	let title: String
	let posterPath: String?
	let cast: [CastMemberViewModel]
}

enum MovieDetailState: Equatable {
	case loading
	case movieDetail(MovieDetailsViewModel)
}

private extension MovieDetails {
    var asMovieDetailsViewModel: MovieDetailsViewModel {
        MovieDetailsViewModel(
                overview: overview,
                title: title,
                posterPath: posterPath,
                cast: cast.map({ $0.asCastMemberViewModel })
        )
    }
}

private extension CastMember {
    var asCastMemberViewModel: CastMemberViewModel {
        CastMemberViewModel(name: name, profilePath: profile_path)
    }
}

final class MovieDetailViewModel: Observer {
	private let stateMachine: MovieDetailsStateMachine
	private let stateSubject: CurrentValueSubject<MovieDetailState, Never>

	var state: AnyPublisher<MovieDetailState, Never> {
		stateSubject
		.removeDuplicates()
		.eraseToAnyPublisher()
	}

    init(stateMachine: MovieDetailsStateMachine) {
        self.stateMachine = stateMachine
		stateSubject = CurrentValueSubject(.loading)
    }

    func bind() {
		stateMachine.register(observer: self)
    }

    func unbind() {
		stateMachine.unRegister(observer: self)
    }

    func loadDetails(_ id: Int) {
		stateMachine.handleAction(action: MovieDetailsAction.LoadMovie(id: Int32(id))) { _, _ in

		}
    }

	// MARK: Observer
	func updateState(state_ state: Any?) {
		switch state {
		case let loading as shared.MovieDetailsState.Loading:
			break
		case let detail as shared.MovieDetailsState.ShowDetails:
			stateSubject.send(.movieDetail(detail.content.asMovieDetailsViewModel))
		default:
			break
		}
	}

}
