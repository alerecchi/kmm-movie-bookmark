//
//  MovieTileCell.swift
//  iosApp
//
//  Created by feanor on 02/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import Combine
import Kingfisher

final class MovieTileCell: UICollectionViewCell {
	@IBOutlet weak var imageView: UIImageView!
	@IBOutlet weak var title: UILabel!

	func configure(movie: MovieTileViewModel) {
		imageView.kf.indicatorType = .activity
		imageView.kf.setImage(
			with: URL(string: movie.posterPath)!,
			options: [
				.transition(.fade(1)),
				.processor(DownsamplingImageProcessor(size: imageView.bounds.size)),
				.scaleFactor(UIScreen.main.scale),
				.cacheOriginalImage
			]
		)

		title.text = nil
	}

	override func prepareForReuse() {
		super.prepareForReuse()
		imageView.kf.cancelDownloadTask()
		title.text = nil
	}
}
